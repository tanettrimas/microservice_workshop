/*
 * Copyright (c) 2019 by Fred George
 * May be used freely except for training; license required for training.
 * @author Fred George  fredgeorge@acm.org
 */

package com.rabbitmq.tutorials.tutorial_three_java

import com.rabbitmq.client.*


class ReceiveLogs {
    companion object {
        const val EXCHANGE_NAME = "logs"
    }
}

fun main() {
    val factory = ConnectionFactory()
    factory.host = "localhost"
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.exchangeDeclare(ReceiveLogs.EXCHANGE_NAME, BuiltinExchangeType.FANOUT)
    val queueName = channel.queueDeclare().queue
    channel.queueBind(queueName, ReceiveLogs.EXCHANGE_NAME, "")

    println(" [*] Waiting for messages. To exit press CTRL+C")

    val consumer = object : DefaultConsumer(channel) {
        override fun handleDelivery(consumerTag: String, envelope: Envelope,
                                    properties: AMQP.BasicProperties, body: ByteArray) {
            val message = String(body, charset("UTF-8"))
            println(" [x] Received '$message'")
        }
    }
    channel.basicConsume(queueName, true, consumer)
}