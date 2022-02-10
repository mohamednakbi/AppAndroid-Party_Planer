package com.example.partyplanner.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Event {
    @SerializedName("_id")
    @Expose
    internal var id: String? = null

    @SerializedName("event_title")
    @Expose
    internal var eventTitle: String? = null

    @SerializedName("venue")
    @Expose
    internal var venue: String? = null

    @SerializedName("maxParticipant")
    @Expose
    internal var maxParticipant: Int? = null

    @SerializedName("date")
    @Expose
    internal var date: String? = null

    @SerializedName("time")
    @Expose
    internal var time: String? = null

    @SerializedName("email")
    @Expose
    internal var email: String? = null

    @SerializedName("__v")
    @Expose
    internal var v: Int? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getEventTitle(): String? {
        return eventTitle
    }

    fun setEventTitle(eventTitle: String?) {
        this.eventTitle = eventTitle
    }

    fun getVenue(): String? {
        return venue
    }

    fun setVenue(venue: String?) {
        this.venue = venue
    }

    fun getMaxParticipant(): Int? {
        return maxParticipant
    }

    fun setMaxParticipant(maxParticipant: Int?) {
        this.maxParticipant = maxParticipant
    }

    fun getDate(): String? {
        return date
    }

    fun setDate(date: String?) {
        this.date = date
    }

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String?) {
        this.time = time
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getV(): Int? {
        return v
    }

    fun setV(v: Int?) {
        this.v = v
    }
}