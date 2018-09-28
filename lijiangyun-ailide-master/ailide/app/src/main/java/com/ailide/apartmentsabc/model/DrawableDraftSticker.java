package com.ailide.apartmentsabc.model;

import com.xiaopo.flying.sticker.Sticker;

import java.io.Serializable;

public class DrawableDraftSticker extends DraftSticker implements Serializable {


    public DrawableDraftSticker(StickerDraft draft, Sticker sticker) {
        super(draft, sticker);
    }

    public DrawableDraftSticker(LabelDraft draft, Sticker sticker) {
        super(draft, sticker);
    }
}
