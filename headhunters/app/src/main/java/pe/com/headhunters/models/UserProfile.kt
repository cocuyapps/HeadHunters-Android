package pe.com.headhunters.models

data class UserProfile(
  var BandDescription: String,
  var BandImgUrl: String,
  var BandMembers: String,
  var BandName: String,
  var UserName: String,
  var BandSample: String

) {
    constructor() : this (
        "",
        "",
        "",
        "",
        "",
        "")
}