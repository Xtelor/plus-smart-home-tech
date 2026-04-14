package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exceptions.ProductInShoppingCartNotInWarehouseException;
import ru.yandex.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.OrderBooking;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.OrderBookingRepository;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final OrderBookingRepository orderBookingRepository;
    private final WarehouseMapper warehouseMapper;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    // Добавление нового товара на склад
    @Override
    @Transactional
    public void addNewProduct(NewProductInWarehouseRequest request) {

        if (warehouseRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар уже существует.");
        }

        WarehouseProduct product = warehouseMapper.toEntity(request);
        product.setQuantity(0L);

        warehouseRepository.save(product);
    }

    // Принятие товара на склад
    @Override
    @Transactional
    public void addToWarehouse(AddProductToWarehouseRequest request) {

        WarehouseProduct product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Данный товар не был добавлен на склад."));

        product.setQuantity(product.getQuantity() + request.getQuantity());

        warehouseRepository.save(product);
    }

    // Предварительная проверка достаточности количества товаров на складе для данной корзины
    @Override
    public BookedProductsDto checkProducts(ShoppingCartDto dto) {

        Map<UUID, Long> products = dto.getProducts();

        List<WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(products.keySet());

        if (warehouseProducts.size() != products.size()) {
            throw new ProductInShoppingCartNotInWarehouseException("Не все товары найдены на складе.");
        }

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (WarehouseProduct product : warehouseProducts) {
            Long requiredQuantity = products.get(product.getProductId());

            if (product.getQuantity() < requiredQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        "Недостаточно товара на складе: " + product.getProductId());
            }

            totalWeight += product.getWeight() * requiredQuantity;
            totalVolume += product.getWidth() * product.getHeight() * product.getDepth() * requiredQuantity;

            if (Objects.equals(Boolean.TRUE, product.getFragile())) {
                hasFragile = true;
            }
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragile)
                .build();
    }

    // Получение адреса склада для расчёта доставки
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    // Передача товаров в доставку
    @Override
    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequest request) {

        orderBookingRepository.findById(request.getOrderId())
                .ifPresent(booking -> {
                    booking.setDeliveryId(request.getDeliveryId());
                    orderBookingRepository.save(booking);
                });
    }

    // Принятие возврата товаров на склад
    @Override
    @Transactional
    public void acceptReturn(Map<UUID, Long> products) {

        List<WarehouseProduct> productList = warehouseRepository.findAllById(products.keySet());

        for (WarehouseProduct product : productList) {
            Long returnQuantity = products.get(product.getProductId());
            product.setQuantity(product.getQuantity() + returnQuantity);
        }

        warehouseRepository.saveAll(productList);
    }

    // Сборка товаров к заказу для подготовки к отправке
    @Override
    @Transactional
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {

        Map<UUID, Long> products = request.getProducts();

        List<WarehouseProduct> productList = warehouseRepository.findAllById(products.keySet());

        if (productList.size() != products.size()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Не все товары найдены на складе.");
        }

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (WarehouseProduct product : productList) {
            Long requiredQuantity = products.get(product.getProductId());

            if (product.getQuantity() < requiredQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        "Недостаточно товара на складе: " + product.getProductId());
            }

            product.setQuantity(product.getQuantity() - requiredQuantity);

            totalWeight += product.getWeight() * requiredQuantity;
            totalVolume += product.getWidth() * product.getHeight() * product.getDepth() * requiredQuantity;

            if (Objects.equals(Boolean.TRUE, product.getFragile())) {
                hasFragile = true;
            }
        }

        warehouseRepository.saveAll(productList);

        orderBookingRepository.save(OrderBooking.builder()
                .orderId(request.getOrderId())
                .build());

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragile)
                .build();
    }
}
