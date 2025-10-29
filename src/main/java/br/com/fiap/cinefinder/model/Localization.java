package br.com.fiap.cinefinder.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Localization {
    private String city;
    private String state;
    private CountryCode countryCode;

    public String getFullAdress() {
        return String.format("%s, %s - %s", city, state, countryCode.getCountryName());
    }

}
