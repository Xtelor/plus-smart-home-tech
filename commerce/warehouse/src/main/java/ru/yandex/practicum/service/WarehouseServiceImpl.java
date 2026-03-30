package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    // Добавление нового товара на склад
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
    @Transactional
    public void addToWarehouse(AddProductToWarehouseRequest request) {

        WarehouseProduct product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Данный товар не был добавлен на склад."));

        product.setQuantity(product.getQuantity() + request.getQuantity());

        warehouseRepository.save(product);
    }

    // Предварительная проверка достаточности количества товаров на складе для данной корзины
    @Transactional
    public BookedProductsDto checkProducts(ShoppingCartDto dto) {

        Map<UUID, Long> products = dto.getProducts();

        List<WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(products.keySet());

        if (warehouseProducts.size() != products.size()) {
            throw new NoSpecifiedProductInWarehouseException("Не все товары найдены на складе.");
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

}
