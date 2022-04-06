package sk.pa3kc.data;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public @Data class FilePaginator extends Paginator {
    private Collection<File> files;
}
