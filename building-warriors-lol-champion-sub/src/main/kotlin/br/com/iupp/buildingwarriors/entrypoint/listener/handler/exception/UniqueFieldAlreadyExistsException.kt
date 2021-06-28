package br.com.iupp.buildingwarriors.entrypoint.listener.handler.exception

class UniqueFieldAlreadyExistsException(val entity : String, val field : String) : RuntimeException()
