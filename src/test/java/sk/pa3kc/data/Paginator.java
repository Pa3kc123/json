package sk.pa3kc.data;

import lombok.Data;
import sk.pa3kc.json.ann.JsonSuperclass;

@JsonSuperclass
public @Data class Paginator {
    protected String kind;
    protected String nextPageToken;
    protected boolean incompleteSearch;
}
