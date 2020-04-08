package com.github.x6ud.ptoy.sprite;

public class BaseSprite {

    public static BaseSprite chain(BaseSprite... sprites) {
        for (int i = 0; i < sprites.length - 1; ++i) {
            BaseSprite curr = sprites[i];
            BaseSprite next = sprites[i + 1];
            curr.addToBottom(next);
        }
        return sprites[0];
    }

    private BaseSprite parent;
    private BaseSprite prev;
    private BaseSprite next;
    private BaseSprite firstChild;
    private BaseSprite lastChild;

    public void addToTop(BaseSprite child) {
        if (child.parent != null) {
            throw new RuntimeException("Sprite already has a parent.");
        }
        child.parent = this;
        if (this.firstChild != null) {
            this.firstChild.prev = child;
            child.next = this.firstChild;
            this.firstChild = child;
        } else {
            this.firstChild = this.lastChild = child;
        }
    }

    public void addToBottom(BaseSprite child) {
        if (child.parent != null) {
            throw new RuntimeException("Sprite already has a parent.");
        }
        child.parent = this;
        if (this.lastChild != null) {
            this.lastChild.next = child;
            child.prev = this.lastChild;
            this.lastChild = child;
        } else {
            this.firstChild = this.lastChild = child;
        }
    }

    public void remove() {
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

    public void removeAllChildren() {
        for (BaseSprite curr = this.lastChild; curr != null; ) {
            BaseSprite next = curr.next;
            curr.remove();
            curr = next;
        }
    }

    public void update(float secs) {
        this.onUpdate(secs);
        for (BaseSprite curr = this.firstChild; curr != null; curr = curr.next) {
            curr.update(secs);
        }
    }

    public void draw() {
        this.onDraw();
        for (BaseSprite curr = this.lastChild; curr != null; curr = curr.prev) {
            curr.draw();
        }
    }

    public BaseSprite parent() {
        return parent;
    }

    public BaseSprite prev() {
        return prev;
    }

    public BaseSprite next() {
        return next;
    }

    public BaseSprite firstChild() {
        return firstChild;
    }

    public BaseSprite lastChild() {
        return lastChild;
    }

    public void onRemoved() {
    }

    public void onUpdate(float secs) {
    }

    public void onDraw() {
    }

}
