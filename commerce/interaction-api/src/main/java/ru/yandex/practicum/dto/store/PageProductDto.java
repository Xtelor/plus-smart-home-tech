package ru.yandex.practicum.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageProductDto {

    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private int size;
    private List<ProductDto> content;
    private int number;
    private List<SortObject> sort;
    private int numberOfElements;
    private boolean empty;
}
