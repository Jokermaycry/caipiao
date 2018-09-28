package com.ailide.apartmentsabc.model;

import java.util.List;

public class MaterialDetail {

    private MaterialDetailTag tag;
    private List<Material> material;

    public MaterialDetailTag getTag() {
        return tag;
    }

    public void setTag(MaterialDetailTag tag) {
        this.tag = tag;
    }

    public List<Material> getMaterial() {
        return material;
    }

    public void setMaterial(List<Material> material) {
        this.material = material;
    }
}
