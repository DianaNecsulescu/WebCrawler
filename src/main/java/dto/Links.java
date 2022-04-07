package dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Links {
    private String parentUrl;
    private Set<Links> childrenUrls;

    public Links(String parentUrl, Set<Links> childrenUrls) {
        this.parentUrl = parentUrl;
        this.childrenUrls = childrenUrls;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public Set<Links> getChildrenUrls() {
        return childrenUrls;
    }

    public void setChildrenUrls(Set<Links> childrenUrls) {
        this.childrenUrls = childrenUrls;
    }

    public static LinksBuilder builder() {
        return new LinksBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Links links = (Links) o;
        return Objects.equals(parentUrl, links.parentUrl) && Objects.equals(childrenUrls, links.childrenUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentUrl, childrenUrls);
    }

    @Override
    public String toString() {
        return "Links{" +
                "parentUrl='" + parentUrl + '\'' +
                ", childrenUrls=" + childrenUrls +
                '}';
    }

    public static class LinksBuilder {
        private String parentUrl;
        private Set<Links> childrenUrls;

        public LinksBuilder parentUrl(String parentUrl) {
            this.parentUrl = parentUrl;
            return this;
        }

        public LinksBuilder childrenUrls(Set<Links> childrenUrls) {
            this.childrenUrls = childrenUrls;
            return this;
        }

        public Links build() {
            return new Links(parentUrl, childrenUrls);
        }
    }
}
