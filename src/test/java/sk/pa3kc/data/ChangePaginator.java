package sk.pa3kc.data;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public @Data class ChangePaginator extends Paginator {
    private List<Change> changes;
}
