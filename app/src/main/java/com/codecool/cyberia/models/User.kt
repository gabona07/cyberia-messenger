package com.codecool.cyberia.models

class User (val uid: String, val username: String, val profileImageUrl: String) {
    constructor() : this("", "", "")
}
