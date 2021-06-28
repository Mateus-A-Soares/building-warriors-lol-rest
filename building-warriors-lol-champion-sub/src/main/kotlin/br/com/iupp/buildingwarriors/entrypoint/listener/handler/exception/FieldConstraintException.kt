package br.com.iupp.buildingwarriors.entrypoint.listener.handler.exception

class FieldConstraintException(val entity: String, val fieldErrors: List<Pair<String, String>>) : RuntimeException()