package com.antonicapart.triumph.errors.creator;

import com.antonicapart.triumph.database.model.Model;
import com.antonicapart.triumph.errors.TriumphError;

public class ModelNotExtendsError extends TriumphError {

    public ModelNotExtendsError(Class<?> classe) {
        super("You'r (" + classe.getName() + ") class not extends Abstract class : " + Model.class.getName());
    }
}
