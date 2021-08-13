package com.arnold.remarques.listeners;

import com.arnold.remarques.entities.Note;
import com.arnold.remarques.entities.Trash;

public interface TrashListeners {

    void onTrashClicked(Trash trash, int position);

}
