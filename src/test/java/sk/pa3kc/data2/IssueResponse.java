package sk.pa3kc.data2;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

import sk.pa3kc.json.ann.JsonKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueResponse {
    private List<Issue> issues;
    @JsonKey("total_count")
    @JsonAlias("total_count")
    private Long totalCount;
    private Long offset;
    private Long limit;
}
