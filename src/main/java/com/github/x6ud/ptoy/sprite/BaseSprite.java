package com.github.x6ud.ptoy.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class BaseSprite {

    private BaseSprite parent;
    private BaseSprite prev;
    private BaseSprite next;
    private BaseSprite firstChild;
    private BaseSprite lastChild;

    public final void addToTop(BaseSprite sprite) {
        if (this.parent != null) {
            throw new RuntimeException("Sprite already has a parent.");
        }
        sprite.parent = this;
        if (this.firstChild != null) {
            this.firstChild.prev = sprite;
            sprite.next = this.firstChild;
            this.firstChild = sprite;
        } else {
            this.firstChild = this.lastChild = sprite;
        }
    }

    public final void addToBottom(BaseSprite sprite) {
        if (this.parent != null) {
            throw new RuntimeException("Sprite already has a parent.");
        }
        sprite.parent = this;
        if (this.lastChild != null) {
            this.lastChild.next = sprite;
            sprite.prev = this.lastChild;
            this.lastChild = sprite;
        } else {
            this.firstChild = this.lastChild = sprite;
        }
    }

    public final void remove() {
        if (this.parent == null) {
            throw new RuntimeException("Sprite hasn't been added to list.");
        }
        if (this.prev != null) {
            this.prev.next = this.next;
        }
        if (this.next != null) {
            this.next.prev = this.prev;
        }
        if (this.parent.firstChild == this) {
            this.parent.firstChild = this.next;
        }
        if (this.parent.lastChild == this) {
            this.parent.lastChild = this.prev;
        }
        this.onRemoved();
    }

    public final void removeAllChildren() {
        for (BaseSprite curr = this.lastChild; curr != null; ) {
            BaseSprite next = curr.next;
            curr.remove();
            curr = next;
        }
    }

    public final void update(float secs) {
        this.onUpdate(secs);
        for (BaseSprite curr = this.firstChild; curr != null; curr = curr.next) {
            curr.update(secs);
        }
    }

    public final void draw(Batch batch) {
        this.onRender(batch);
        for (BaseSprite curr = this.lastChild; curr != null; curr = curr.prev) {
            curr.draw(batch);
        }
    }

    public final BaseSprite parent() {
        return parent;
    }

    public final BaseSprite prev() {
        return prev;
    }

    public final BaseSprite next() {
        return next;
    }

    public final BaseSprite firstChild() {
        return firstChild;
    }

    public final BaseSprite lastChild() {
        return lastChild;
    }

    public abstract void onRemoved();

    public abstract void onUpdate(float secs);

    public abstract void onRender(Batch batch);

}
