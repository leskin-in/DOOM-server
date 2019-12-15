#!/usr/bin/env python3

import tkinter
import re


def main():
    Application(master=tkinter.Tk()).mainloop()


HELLO_TEXT = """Welcome to DOOM-592!

This game is a simple multiplayer platformer.

Controls:
* WASD or arrows: Movement
* 'b': Place a bomb
* 'q': Leave the game (suicide)

The winner is the player who outlived everyone else.

Good luck!

To play, enter the running server address:"""


stored_address = None


def _store_address(address: str) -> None:
    """
    Store an address (URL) in the global variable
    """
    global stored_address
    stored_address = address


class Application(tkinter.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.master = master
        self._create_widgets()

    def _create_widgets(self):
        """
        Initialize the widgest of this application
        """
        self.heading = tkinter.Label(self.master, text='Doom 592', font='Helvetica 12 bold', borderwidth=2, relief='solid', background='#000000', foreground='#ff0000')
        self.heading.grid(row=0, column=0, columnspan=2)

        self.text = tkinter.Label(self.master, text=HELLO_TEXT, font='Helvetica 12')
        self.text.grid(row=1, column=0, columnspan=2)

        self.address = tkinter.Entry(self.master, font='Helvetica 14')
        self.address.grid(row=2, column=0, columnspan=1)

        self.play_button = tkinter.Button(self.master, text="Play", command=self.set_address, font='Helvetica 12 bold italic')
        self.play_button.grid(row=2, column=1, columnspan=1, sticky=tkinter.E)

        self.master.bind('<Return>', self.set_address_event)

        self.address.focus()

    def set_address_event(self, event):
        self.set_address()

    def set_address(self):
        address_input = self.address.get()
        if address_input is None or address_input.isspace() or address_input == '':
            return

        _store_address(address_input)
        self.master.destroy()


if __name__ == '__main__':
    main()
    print(stored_address)
