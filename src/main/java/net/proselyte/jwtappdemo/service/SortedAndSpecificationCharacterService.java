package net.proselyte.jwtappdemo.service;

import net.proselyte.jwtappdemo.model.Character;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Service
public class SortedAndSpecificationCharacterService {//вынес в отдельный сервис,
                                                     //так как спецификации/сортировка могут понадобится не в одном классе

    public Specification<Character> checkComics2Character(List<Character> characters, String slug) {
        return new Specification<Character>() {
            @Override
            public Predicate toPredicate(Root<Character> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        };
    }

    public Specification<Character> checkComics(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("comics_string"),"%"+name+"%");
        //TODO: не выполняется условие первой нормальной формы
    }

    public Specification<Character> checkName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%"+name+"%");
    }

    public Specification<Character> checkSlug(String slug) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("slug"),"%"+slug+"%");
    }

    public Specification<Character> checkTitle(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),"%"+name+"%");
    }

    public List<Character> sort(List<Character> characters, Integer sortIndex){
        List<Character> modifiableList = new ArrayList<>(characters);
        switch (sortIndex){
            case 1:
                modifiableList.sort(Comparator.comparing(o -> o.getName().toLowerCase()));
                break;
            case 2:
                modifiableList.sort(Comparator.comparing(o -> o.getSlug().toLowerCase()));
                break;
            case 3:
                modifiableList.sort(Comparator.comparing(o -> o.getTitle().toLowerCase()));
                break;
        }
        return modifiableList;
    }

    public String forQuery(String object){//каждая строка содержит ""
        return object == null ? "" : object;
    }
}
