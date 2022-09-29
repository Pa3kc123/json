package sk.pa3kc.data2;

import java.util.List;

import sk.pa3kc.json.ann.JsonKey;

public class IssueResponse {
    private List<Issue> issues;
    @JsonKey("total_count")
    private Long totalCount;
    private Long offset;
    private Long limit;

    public List<Issue> getIssues() { return issues; }
    public void setIssues(List<Issue> value) { this.issues = value; }

    public Long getTotalCount() { return totalCount; }
    public void setTotalCount(Long value) { this.totalCount = value; }

    public Long getOffset() { return offset; }
    public void setOffset(Long value) { this.offset = value; }

    public Long getLimit() { return limit; }
    public void setLimit(Long value) { this.limit = value; }
}
