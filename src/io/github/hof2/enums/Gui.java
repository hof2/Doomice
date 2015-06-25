package io.github.hof2.enums;

import io.github.hof2.controls.HeadmasterControl;
import io.github.hof2.controls.StudentControl;

/**
 * @author Matthias Hofreiter
 * @since May 22, 2015
 * Defines all {@link Gui Guis} for {@link SimpleGui}.
 */
public enum Gui {

    /**
     * The screen used to choose between {@link HeadmasterControl Headmaster}
     * and {@link StudentControl Student}.
     */
    ChoosePlayer, 
    /**
     * The screen used to connect to a server or start a personal server.
     */
    HandleMultiplayer
}
