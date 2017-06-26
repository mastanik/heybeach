package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.autocomplete.PrefixTree;
import com.daimler.heybeach.backend.dao.HashtagDao;
import com.daimler.heybeach.backend.entities.Hashtag;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HashtagService {

    private static final Logger logger = LoggerFactory.getLogger(HashtagService.class);

    @Value("${hashtag.suggestion.count}")
    private Integer hashtagSuggestionCount;

    @Autowired
    private HashtagDao hashtagDao;

    private PrefixTree hashtagTree;

    public List<String> suggest(String hashtag) {
        List<PrefixTree.Entry> hashtagEntry = hashtagTree.list(hashtag);
        List<String> topHashtags = hashtagEntry.stream()
                .sorted((o1, o2) -> o2.getCount().compareTo(o1.getCount()))
                .limit(hashtagSuggestionCount)
                .map(PrefixTree.Entry::getValue)
                .collect(Collectors.toList());
        return topHashtags;
    }

    public void add(List<String> hashtags) {
        for (String hashtag : hashtags) {
            hashtagTree.add(hashtag);
        }
    }

    @PostConstruct
    public void run() throws ValidationException {
        hashtagTree = new PrefixTree();
        try {
            List<Hashtag> hashtags = hashtagDao.findAll();
            hashtags.stream().forEach(hashtag -> hashtagTree.add(hashtag.getName()));
        } catch (DaoException e) {
            logger.error("Error loading hashtags to cache");
        }

    }
}
