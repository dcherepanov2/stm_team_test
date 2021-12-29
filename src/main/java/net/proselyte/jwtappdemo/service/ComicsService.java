package net.proselyte.jwtappdemo.service;


import net.proselyte.jwtappdemo.dto.ComicsAddRequest;
import net.proselyte.jwtappdemo.exception.characterException.CharacterException;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsException;
import net.proselyte.jwtappdemo.model.Character;
import net.proselyte.jwtappdemo.model.Comics;
import net.proselyte.jwtappdemo.repository.CharacterRepo;
import net.proselyte.jwtappdemo.repository.ComicsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ComicsService {

    private final ComicsRepo comicsRepo;
    private final CharacterRepo characterRepo;
    private final SortedAndSpecificationComicsService sortedComics;
    private final SortedAndSpecificationCharacterService sortedCharacters;

    @Autowired
    public ComicsService(ComicsRepo comicsRepo, CharacterRepo characterRepo, SortedAndSpecificationComicsService sortedComics, SortedAndSpecificationCharacterService sortedCharacters) {
        this.comicsRepo = comicsRepo;
        this.characterRepo = characterRepo;
        this.sortedComics = sortedComics;
        this.sortedCharacters = sortedCharacters;
    }


    public Comics getCharacterBySlug(String slug) throws ComicsException {
        Comics comics = comicsRepo.findCharacterBySlug(slug);
        if(comics == null)
            throw new ComicsException("Ooo...It seems that no such item has been found. Try to refine your filtering options");
        return comics;
    }


    public List<Comics> findAll(Integer offset, Integer limit, Integer sortIndex, String name, String description, String authors, String painter) {
            Pageable page = PageRequest.of(offset, limit);
            name = name == null ? "" : name;
            description = description == null ? "" : description;
            authors = authors == null ? "" : authors;
            painter = painter == null ? "" : painter;
            List<Comics> comics = comicsRepo.findAll(
                            Specification//TODO: добавить еще столбец со связями (character)
                            .where(sortedComics.checkName(name))
                            .and(sortedComics.checkDescription(description))
                            .and(sortedComics.checkAuthors(authors))
                            .and(sortedComics.checkPainter(painter))
                    ,page).getContent();
            if(sortIndex != null)
                sortedComics.sortedComics(comics,sortIndex);
            return comics;
        }

    public List<Character> getCharacterOfComicsById(Integer offset, Integer limit, String slug,
                                         String name, String title, Integer sortIndex, String slugCharacter)
            throws ComicsException, CharacterException {
        Pageable page = PageRequest.of(offset, limit);
        Comics comics = comicsRepo.findComicsBySlug(slug);
        if (slug == null || slug.equals(""))
            throw new ComicsException("Slug should not be empty");
        if (comics == null)
            throw new ComicsException("Ooo... Comics with slug " + slug + " not found");
        name = name == null ? "" : name;
        title = title == null ? "" : title;
        slugCharacter = slugCharacter == null ? "" : slugCharacter;
        List<Character> charactersOfComics = characterRepo
                                                .findAll(Specification
                                                        .where(sortedCharacters.checkComics(slug))
                                                        .and(sortedCharacters.checkSlug(slugCharacter))
                                                        .and(sortedCharacters.checkTitle(title))
                                                        .and(sortedCharacters.checkName(name)), page)
                                                        .getContent();
        if(charactersOfComics.size() == 0)
            throw new CharacterException("Ooo... Comics with characters slug " + slug + " not found");
        if(sortIndex != null)
            sortedCharacters.sort(charactersOfComics,sortIndex);
        return charactersOfComics;
    }

    public Comics addComics(ComicsAddRequest comics) {
        Comics comicsData = new Comics();
        comicsData.setImagePath("null");
        comicsData.setSlug(UUID.randomUUID().toString().substring(0,16));
        comicsData.setName(comics.getName());
        comicsData.setPainter(comics.getPainter());
        comicsData.setDescription(comics.getDescription());
        comicsData.setAuthors(comics.getAuthors());
        comicsData.setCharacter_slug("");
        comicsRepo.save(comicsData);
        return comicsData;
    }
}

