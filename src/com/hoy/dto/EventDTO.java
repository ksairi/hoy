package com.hoy.dto;

import android.content.res.Configuration;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.utilities.MilongaCollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LDicesaro
 */
public class EventDTO implements Serializable {

	private static final long serialVersionUID = 8758142710877401485L;

	private Integer pk;
	private String name;
	private String genre;
	private String daysOfTheWeek;
	private String occurrencesInMonth;
	private String startTime;
	private String endTime;
	private String price;
	private String currency;
	private String organizersNames;
	private String profile;
	private String details;
	private String emailContact;
	private String website;
	private String phones;
	private Boolean reservationAdvicedFlag;
	private Boolean offersClassFlag;
	private String firstClassStarts;
	private String lastClassEnds;
	private String classContentAndPricingDetails;
	private String specialEventFlag;
	private String photoOLogoThumbnailURL;
	private String rating;
	private String infoSource;
	private String lastUpdated;
	private String date;
	private String commentsOfTheDay;
	private String ratingOfTheDay;
	private String startDateTime;
	private String endDateTime;
	private String firstClassStartsDateTime;
	private String lastClassEndsDateTime;
	private String nameOfPlace;
	private String streetLine1;
	private String streetLine2;
	private String familiarNameOfArea;
	private String city;
	private String country;
	private String latitude;
	private String longitude;
	private String howToGetThere;
	private String more;
	private String areaID;
	private String timeZone;
	private Boolean eventCancelledFlag;

	public Integer getPk() {
		return pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDaysOfTheWeek() {
		return daysOfTheWeek;
	}

	public void setDaysOfTheWeek(String daysOfTheWeek) {
		this.daysOfTheWeek = daysOfTheWeek;
	}

	public String getOccurrencesInMonth() {
		return occurrencesInMonth;
	}

	public void setOccurrencesInMonth(String occurrencesInMonth) {
		this.occurrencesInMonth = occurrencesInMonth;
	}

	public String getStartTime() {

		if(startTime != null && startTime.length() == MilongaHoyConstants.TIME_WITH_SECONDS_LENGTH){
			return startTime.substring(0,startTime.length()-3);
		}

		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		if(endTime != null && endDateTime.length() == MilongaHoyConstants.TIME_WITH_SECONDS_LENGTH){
			return endTime.substring(0,endTime.length()-3);
		}

		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOrganizersNames() {
		return organizersNames;
	}

	public void setOrganizersNames(String organizersNames) {
		this.organizersNames = organizersNames;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getEmailContact() {
		return emailContact;
	}

	public void setEmailContact(String emailContact) {
		this.emailContact = emailContact;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public Boolean getReservationAdvicedFlag() {
		return reservationAdvicedFlag;
	}

	public void setReservationAdvicedFlag(Boolean reservationAdvicedFlag) {
		this.reservationAdvicedFlag = reservationAdvicedFlag;
	}

	public Boolean getOffersClassFlag() {
		return offersClassFlag;
	}

	public void setOffersClassFlag(Boolean offersClassFlag) {
		this.offersClassFlag = offersClassFlag;
	}

	public String getFirstClassStarts() {
		return firstClassStarts;
	}

	public void setFirstClassStarts(String firstClassStarts) {
		this.firstClassStarts = firstClassStarts;
	}

	public String getLastClassEnds() {
		return lastClassEnds;
	}

	public void setLastClassEnds(String lastClassEnds) {
		this.lastClassEnds = lastClassEnds;
	}

	public String getClassContentAndPricingDetails() {
		return classContentAndPricingDetails;
	}

	public void setClassContentAndPricingDetails(String classContentAndPricingDetails) {
		this.classContentAndPricingDetails = classContentAndPricingDetails;
	}

	public String getSpecialEventFlag() {
		return specialEventFlag;
	}

	public void setSpecialEventFlag(String specialEventFlag) {
		this.specialEventFlag = specialEventFlag;
	}

	public String getPhotoOLogoThumbnailURL() {
		return photoOLogoThumbnailURL;
	}

	public void setPhotoOLogoThumbnailURL(String photoOLogoThumbnailURL) {
		this.photoOLogoThumbnailURL = photoOLogoThumbnailURL;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getInfoSource() {
		return infoSource;
	}

	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCommentsOfTheDay() {
		return commentsOfTheDay;
	}

	public void setCommentsOfTheDay(String commentsOfTheDay) {
		this.commentsOfTheDay = commentsOfTheDay;
	}

	public String getRatingOfTheDay() {
		return ratingOfTheDay;
	}

	public void setRatingOfTheDay(String ratingOfTheDay) {
		this.ratingOfTheDay = ratingOfTheDay;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getFirstClassStartsDateTime() {
		return firstClassStartsDateTime;
	}

	public void setFirstClassStartsDateTime(String firstClassStartsDateTime) {
		this.firstClassStartsDateTime = firstClassStartsDateTime;
	}

	public String getLastClassEndsDateTime() {
		return lastClassEndsDateTime;
	}

	public void setLastClassEndsDateTime(String lastClassEndsDateTime) {
		this.lastClassEndsDateTime = lastClassEndsDateTime;
	}

	public String getNameOfPlace() {
		return nameOfPlace;
	}

	public void setNameOfPlace(String nameOfPlace) {
		this.nameOfPlace = nameOfPlace;
	}

	public String getStreetLine1() {
		return streetLine1;
	}

	public void setStreetLine1(String streetLine1) {
		this.streetLine1 = streetLine1;
	}

	public String getStreetLine2() {
		return streetLine2;
	}

	public void setStreetLine2(String streetLine2) {
		this.streetLine2 = streetLine2;
	}

	public String getFamiliarNameOfArea() {
		return familiarNameOfArea;
	}

	public void setFamiliarNameOfArea(String familiarNameOfArea) {
		this.familiarNameOfArea = familiarNameOfArea;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getHowToGetThere() {
		return howToGetThere;
	}

	public void setHowToGetThere(String howToGetThere) {
		this.howToGetThere = howToGetThere;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	public String getAreaID() {
		return areaID;
	}

	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Boolean getEventCancelledFlag() {
		return eventCancelledFlag;
	}

	public void setEventCancelledFlag(Boolean eventCancelledFlag) {
		this.eventCancelledFlag = eventCancelledFlag;
	}

}
