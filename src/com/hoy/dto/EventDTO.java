package com.hoy.dto;

import android.os.Parcel;
import android.os.Parcelable;
import com.hoy.utilities.DateUtils;

import java.text.ParseException;

/**
 * @author LDicesaro
 */
public class EventDTO implements Parcelable, Comparable<EventDTO> {

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
	private String reservationAdvicedFlag;
	private String offersClassFlag;
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
	private String eventCancelledFlag;
	private String serverLastUpdateTime;

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

		return DateUtils.getTimeFromTimeString(startTime);

	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {

		return DateUtils.getTimeFromTimeString(endTime);

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

	public String getReservationAdvicedFlag() {
		return reservationAdvicedFlag;
	}

	public void setReservationAdvicedFlag(String reservationAdvicedFlag) {
		this.reservationAdvicedFlag = reservationAdvicedFlag;
	}

	public String getOffersClassFlag() {
		return offersClassFlag;
	}

	public void setOffersClassFlag(String offersClassFlag) {
		this.offersClassFlag = offersClassFlag;
	}

	public String getFirstClassStarts() {

		return DateUtils.getTimeFromTimeString(firstClassStarts);
	}

	public void setFirstClassStarts(String firstClassStarts) {
		this.firstClassStarts = firstClassStarts;
	}

	public String getLastClassEnds() {
		return DateUtils.getTimeFromTimeString(lastClassEnds);
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

		return DateUtils.getTimeFromTimeString(lastClassEndsDateTime);
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

	public String getEventCancelledFlag() {
		return eventCancelledFlag;
	}

	public void setEventCancelledFlag(String eventCancelledFlag) {
		this.eventCancelledFlag = eventCancelledFlag;
	}

	public int describeContents() {
		return this.hashCode();
	}

	public String getServerLastUpdateTime() {
		return serverLastUpdateTime;
	}

	public void setServerLastUpdateTime(String serverLastUpdateTime) {
		this.serverLastUpdateTime = serverLastUpdateTime;
	}

	public void writeToParcel(Parcel parcel, int i) {

		parcel.writeInt(pk);
		parcel.writeString(name);
		parcel.writeString(genre);
		parcel.writeString(daysOfTheWeek);
		parcel.writeString(occurrencesInMonth);
		parcel.writeString(startTime);
		parcel.writeString(endTime);
		parcel.writeString(price);
		parcel.writeString(currency);
		parcel.writeString(organizersNames);
		parcel.writeString(profile);
		parcel.writeString(details);
		parcel.writeString(emailContact);
		parcel.writeString(website);
		parcel.writeString(phones);
		parcel.writeString(offersClassFlag);
		parcel.writeString(reservationAdvicedFlag);
		parcel.writeString(firstClassStarts);
		parcel.writeString(lastClassEnds);
		parcel.writeString(classContentAndPricingDetails);
		parcel.writeString(specialEventFlag);
		parcel.writeString(photoOLogoThumbnailURL);
		parcel.writeString(rating);
		parcel.writeString(infoSource);
		parcel.writeString(lastUpdated);
		parcel.writeString(date);
		parcel.writeString(commentsOfTheDay);
		parcel.writeString(ratingOfTheDay);
		parcel.writeString(startDateTime);
		parcel.writeString(endDateTime);
		parcel.writeString(firstClassStartsDateTime);
		parcel.writeString(lastClassEndsDateTime);
		parcel.writeString(nameOfPlace);
		parcel.writeString(streetLine1);
		parcel.writeString(streetLine2);
		parcel.writeString(familiarNameOfArea);
		parcel.writeString(city);
		parcel.writeString(country);
		parcel.writeString(latitude);
		parcel.writeString(longitude);
		parcel.writeString(howToGetThere);
		parcel.writeString(more);
		parcel.writeString(areaID);
		parcel.writeString(timeZone);
		parcel.writeString(eventCancelledFlag);
		parcel.writeString(serverLastUpdateTime);

	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public EventDTO createFromParcel(Parcel in) {
			return new EventDTO(in);
		}

		public EventDTO[] newArray(int size) {
			return new EventDTO[size];
		}
	};


	public EventDTO(Parcel parcel) {

		pk = parcel.readInt();
		name = parcel.readString();
		genre = parcel.readString();
		daysOfTheWeek = parcel.readString();
		occurrencesInMonth = parcel.readString();
		startTime = parcel.readString();
		endTime = parcel.readString();
		price = parcel.readString();
		currency = parcel.readString();
		organizersNames = parcel.readString();
		profile = parcel.readString();
		details = parcel.readString();
		emailContact = parcel.readString();
		website = parcel.readString();
		phones = parcel.readString();
		offersClassFlag = parcel.readString();
		reservationAdvicedFlag = parcel.readString();
		firstClassStarts = parcel.readString();
		lastClassEnds = parcel.readString();
		classContentAndPricingDetails = parcel.readString();
		specialEventFlag = parcel.readString();
		photoOLogoThumbnailURL = parcel.readString();
		rating = parcel.readString();
		infoSource = parcel.readString();
		lastUpdated = parcel.readString();
		date = parcel.readString();
		commentsOfTheDay = parcel.readString();
		ratingOfTheDay = parcel.readString();
		startDateTime = parcel.readString();
		endDateTime = parcel.readString();
		firstClassStartsDateTime = parcel.readString();
		lastClassEndsDateTime = parcel.readString();
		nameOfPlace = parcel.readString();
		streetLine1 = parcel.readString();
		streetLine2 = parcel.readString();
		familiarNameOfArea = parcel.readString();
		city = parcel.readString();
		country = parcel.readString();
		latitude = parcel.readString();
		longitude = parcel.readString();
		howToGetThere = parcel.readString();
		more = parcel.readString();
		areaID = parcel.readString();
		timeZone = parcel.readString();
		eventCancelledFlag = parcel.readString();
		serverLastUpdateTime = parcel.readString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EventDTO eventDTO = (EventDTO) o;

		if (pk != null ? !pk.equals(eventDTO.pk) : eventDTO.pk != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return pk != null ? pk.hashCode() : 0;
	}

	public int compareTo(EventDTO compareEventDTO) {

			//ascending order
			return this.getStartDateTime().compareTo(compareEventDTO.getStartDateTime());

			//descending order
			//return compareEventDTO - this.getDate();

	}
}
