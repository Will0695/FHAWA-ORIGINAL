package com.example.pruebafinalis;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterLetters implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        // Recorre los caracteres ingresados
        for (int i = start; i < end; i++) {
            // Si el carácter no es una letra, no se permite
            if (!Character.isLetter(source.charAt(i))) {
                return "";
            }
        }
        // Si todos los caracteres son letras, se acepta la entrada
        return null;
    }
}
