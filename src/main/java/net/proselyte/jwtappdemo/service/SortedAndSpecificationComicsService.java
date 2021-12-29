package net.proselyte.jwtappdemo.service;

import net.proselyte.jwtappdemo.model.Character;
import net.proselyte.jwtappdemo.model.Comics;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SortedAndSpecificationComicsService {//вынес в отдельный сервис,
                                                  //так как спецификации/сортировка могут понадобится не в одном классе

    public List<Comics> sortedComics(List<Comics> allComics, Integer sortIndex){
        List<Comics> modify = new ArrayList<>(allComics);
        switch (sortIndex){
            case 1:
                modify.sort(Comparator.comparing(o -> o.getName().toLowerCase()));
            case 2:
                modify.sort(Comparator.comparing(o -> o.getSlug().toLowerCase()));
            case 3:
                modify.sort(Comparator.comparing(o -> o.getDescription().toLowerCase()));
            case 4:
                modify.sort(Comparator.comparing(o -> o.getPainter().toLowerCase()));
            case 5:
                modify.sort(Comparator.comparing(o -> o.getAuthors().toLowerCase()));
        }
        return modify;
    }

    public Specification<Comics> checkCharacter(Character character) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("character_slug"),character.getSlug());
    }//TODO: не выполняется условие первой нормальной формы

    public Specification<Comics> checkName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%"+name+"%");
    }

    public Specification<Comics> checkSlug(String slug) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("slug"),"%"+slug+"%");
    }

    public Specification<Comics> checkAuthors(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("authors"),"%"+name+"%");
    }

    public Specification<Comics> checkPainter(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("painter"),"%"+name+"%");
    }

    public Specification<Comics> checkDescription(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"),"%"+description+"%");
    }
}
