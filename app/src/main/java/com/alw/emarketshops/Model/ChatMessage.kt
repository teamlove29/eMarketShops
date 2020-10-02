package com.alw.emarketshops.Model

class ChatMessage(
    var message: String,
    var messageId: String,
    var recipient:String,
    var sender:String,
    var time: MutableMap<String, String>
                    )