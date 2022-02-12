package com.example.bookstore.payload;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.ImmutableBook;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Value.Immutable
@Value.Enclosing
@JsonDeserialize(as = ImmutableGoogleBookGetResponse.class)
public interface GoogleBookGetResponse {
    List<Item> getItems();

    @Value.Immutable
    @JsonDeserialize(as = ImmutableGoogleBookGetResponse.Item.class)
    interface Item {
        String getId();

        VolumeInfo getVolumeInfo();

        @Value.Immutable
        @JsonDeserialize(as = ImmutableGoogleBookGetResponse.VolumeInfo.class)
        interface VolumeInfo {
            @Nullable
            @Value.Default
            default String getTitle() {
                return "(no title)";
            }

            List<String> getAuthors();

            static Book to(VolumeInfo volumeInfo) { // FIXME assuming VolumeInfo is not null
                String title = volumeInfo.getTitle();
                List<String> authors = volumeInfo.getAuthors();

                return ImmutableBook.builder()
                        .title(title)
                        .authors(authors)
                        .build();
            }
        }
    }

    static List<Book> to(List<Item> items) {
        return items.stream()
                .filter(item -> item.getVolumeInfo() != null)
                .map(item -> Item.VolumeInfo.to(item.getVolumeInfo()))
                .collect(Collectors.toList());
    }
}
