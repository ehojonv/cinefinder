package br.com.fiap.cinefinder.model;

import lombok.Getter;

@Getter
public enum CountryCode {
    US("Estados Unidos", "USA", "en"),
    BR("Brasil", "BRA", "pt"),
    AR("Argentina", "ARG", "es");

    private String countryName;
    private String alpha3Code;
    private String languageCode;

    CountryCode(String countryName, String alpha3, String languageCode) {
        this.countryName = countryName;
        this.alpha3Code = alpha3;
        this.languageCode = languageCode;
    }
}
