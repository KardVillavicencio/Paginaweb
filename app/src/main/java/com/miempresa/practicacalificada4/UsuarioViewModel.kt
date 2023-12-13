package com.miempresa.practicacalificada4
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Usuario(val nombre: String, val email: String)

class UsuarioViewModel : ViewModel() {
    // LiveData para almacenar la información del usuario
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario

    // Método para establecer la información del usuario
    fun setUsuario(usuario: Usuario) {
        _usuario.value = usuario
    }
}
