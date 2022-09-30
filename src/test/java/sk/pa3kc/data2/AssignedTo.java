package sk.pa3kc.data2;

import com.fasterxml.jackson.annotation.JsonAlias;

import sk.pa3kc.json.ann.JsonKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignedTo {
    private Long id;
    private String name;
    @JsonKey("avatar_urls")
    @JsonAlias("avatar_urls")
    private AvatarUrls avatarUrls;
}
