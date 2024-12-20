package com.homemylove.core.cs;

import lombok.Getter;

public class CS {
    public interface SORTED_WAY{
        String BY_TIME = "byTime";
        String BY_LIKES = "byLikes";
    }

    @Getter
    public enum REDIS_KEY{
        SEARCH_AUTHOR_HISTORY("search_author_history:"),
        SEARCH_ARTICLE_HISTORY("search_article_history:"),
        AUTHOR_POPULARITY("author_popularity"),
        ARTICLE_POPULARITY("article_popularity"),
        AUTHOR_POPULARITY_SNAPSHOT("author_popularity_snapshot"),
        ARTICLE_POPULARITY_SNAPSHOT("article_popularity_snapshot");
        private final String key;
        REDIS_KEY(String key) {
            this.key = key;
        }
    }

    @Getter
    public enum TEXT_TYPE{
        TEXT,FILE;
    }
}
