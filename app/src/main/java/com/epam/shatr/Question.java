package com.epam.shatr;

import android.content.Context;
import android.view.View;

public class Question extends View {

    private String question;

    public Question(Context context, String question) {
        super(context);
        this.question = question;
    }
}
