package net.proselyte.jwtappdemo.service;

import net.proselyte.jwtappdemo.dto.CharacterDtoRequest;
import net.proselyte.jwtappdemo.exception.characterException.CharacterException;
import net.proselyte.jwtappdemo.exception.comicsException.ComicsAddException;
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
public class CharacterService {

    private final CharacterRepo characterRepo;
    private final ComicsRepo comicsRepo;
    private final SortedAndSpecificationCharacterService sorted;
    private final SortedAndSpecificationComicsService sortedComics;

    @Autowired
    public CharacterService(CharacterRepo characterRepo, ComicsRepo comicsRepo, SortedAndSpecificationCharacterService sorted, SortedAndSpecificationComicsService sortedComics) {
        this.characterRepo = characterRepo;
        this.comicsRepo = comicsRepo;
        this.sorted = sorted;
        this.sortedComics = sortedComics;
    }

    public Character findCharacterById(String slug) {
        return characterRepo.findCharacterBySlug(slug);
    }

    public List<Character> findAll(Integer offset, Integer limit, Integer sortIndex,
                                   String name, String title, String slug) {
        Pageable pageable = PageRequest.of(offset, limit);
        name = sorted.forQuery(name);
        title = sorted.forQuery(title);
        slug = sorted.forQuery(slug);
        List<Character> allCharacters = characterRepo.findAll(
                Specification.where(
                        sorted.checkName(name))
                        .and(sorted.checkSlug(slug))
                        .and(sorted.checkTitle(title)), pageable)
                .getContent();
        if (sortIndex != null)
            allCharacters = sorted.sort(allCharacters, sortIndex);
        return allCharacters;
    }

    public List<Comics> findComicsOfCharacterBySlug(Integer offset, Integer limit, Integer sortIndex,
                                                    String slugCharacter, String name, String description, String authors,
                                                    String painter)
            throws CharacterException {
        Pageable pageable = PageRequest.of(offset, limit);
        name = name == null ? "" : name;
        description = description == null ? "" : description;
        authors = authors == null ? "" : authors;
        painter = painter == null ? "" : painter;
        Character characterLocal = characterRepo.findCharacterBySlug(slugCharacter);
        if (characterLocal == null)
            throw new CharacterException("Ooo... Character with slug " + slugCharacter + " not found");
        List<Comics> comics = comicsRepo.findAll(Specification
                        .where(sortedComics.checkName(name))
                        .and(sortedComics.checkDescription(description))
                        .and(sortedComics.checkAuthors(authors))
                        .and(sortedComics.checkPainter(painter))
                        .and(sortedComics.checkCharacter(characterLocal))
                , pageable).getContent();
        if (sortIndex != null)
            comics = sortedComics.sortedComics(comics, sortIndex);
        return comics;
    }

    public Character addCharacter(CharacterDtoRequest request) {
        Character character = new Character();
        character.setName(request.getName());
        character.setTitle(request.getTitle());
        character.setSlug(UUID.randomUUID().toString().substring(0, 14));
        character.setComics_string("");
        characterRepo.save(character);
        return character;
    }

    public Character addComicsCharacter(String slug, String comics)
            throws CharacterException, ComicsException, ComicsAddException {
        Character characterLocal = characterRepo.findCharacterBySlug(slug);
        if (characterLocal == null)
            throw new CharacterException("Ooo... Character with slug " + slug + " not found");
        StringBuilder allComicsFound = new StringBuilder(characterLocal.getComics_string());
        List<Comics> comicsList = characterLocal.getComics();
        for (String slugComics : comics.split(",")) {
            Comics comics1 = comicsRepo.findComicsBySlug(slugComics);
            if (comics1 == null)
                throw new ComicsException("Ooo... Comics with slug" + slugComics + "not found.");
            if (comicsList.contains(comics1))
                throw new ComicsAddException("Comics with slug: " + slugComics + " already assigned to this superhero.");
            comics1.setCharacter_slug(comics1.getCharacter_slug() + slug);
            comicsRepo.save(comics1);
            comicsList.add(comics1);
            allComicsFound.append(comics1.getSlug());
        }
        if (allComicsFound.toString() == null || allComicsFound.toString().equals(""))
            allComicsFound = new StringBuilder("");
        characterLocal.setComics_string(allComicsFound.toString());
        characterLocal.setComics(comicsList);
        characterRepo.save(characterLocal);
        return characterLocal;
    }
}
