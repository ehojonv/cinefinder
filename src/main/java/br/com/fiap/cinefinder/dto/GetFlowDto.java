package br.com.fiap.cinefinder.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.cinefinder.model.Flow;

public record GetFlowDto(
        Long id,
        String title,
        GetUserDto author,
        List<MovieRefDto> movies
) {

    public static GetFlowDto fromFlow(Flow f) {
        if (f == null) return null;
        var movies = f.getMovies() == null ? java.util.Collections.<MovieRefDto>emptyList()
                : f.getMovies().stream().map(MovieRefDto::fromMovie).collect(Collectors.toList());

        return new GetFlowDto(f.getId(), f.getTitle(), GetUserDto.fromAppUser(f.getAuthor()), movies);
    }

}
