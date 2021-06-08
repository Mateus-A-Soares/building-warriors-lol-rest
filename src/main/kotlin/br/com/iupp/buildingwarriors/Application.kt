package br.com.iupp.buildingwarriors

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("br.com.iupp.buildingwarriors")
        .start()
}
