#!/usr/bin/env python3

import tkinter
import subprocess
import os


JAR_LOCATION = '/usr/share/d592-client/d592client.jar'


def main():
    app = Application(master=tkinter.Tk())
    app.mainloop()
    if app.stored_address is None:
        return

    subprocess.run(['java', '-jar', JAR_LOCATION, app.stored_address])


class Application(tkinter.Frame):
    _HELLO_TEXT = [
        """Welcome to DOOM-592,
a simple multiplayer platformer!

Controls:""",
        """* WASD or arrows: Movement
* 'b': Place a bomb
* ESC: Leave the game (suicide)""",
        """

To win, become the last player alive.

Good luck!

To play,
enter the running server address:"""
    ]

    def __init__(self, master):
        super().__init__(master)
        self._master = master
        self._set_master_properties()
        self._create_widgets()

        self.stored_address = None

    def _create_widgets(self):
        """
        Initialize the widgest of this application
        """
        self.heading = tkinter.Label(self._master, text='Doom 592', font='Helvetica 12 bold', borderwidth=2,
                                     relief='solid', background='#000000', foreground='#ff0000')
        self.heading.grid(row=0, column=0, columnspan=2, pady=10)

        self.text1 = tkinter.Label(self._master, text=Application._HELLO_TEXT[0], font='Helvetica 12')
        self.text1.grid(row=1, column=0, columnspan=2)

        self.text2 = tkinter.Label(self._master, text=Application._HELLO_TEXT[1], font='Helvetica 12', justify='left')
        self.text2.grid(row=2, column=0, columnspan=2)

        self.text3 = tkinter.Label(self._master, text=Application._HELLO_TEXT[2], font='Helvetica 12')
        self.text3.grid(row=3, column=0, columnspan=2)

        self.address = tkinter.Entry(self._master, font='Helvetica 14')
        self.address.grid(row=4, column=0, columnspan=1)

        self.play_button = tkinter.Button(self._master, text="Play", command=self.set_address,
                                          font='Helvetica 12 bold italic')
        self.play_button.grid(row=4, column=1, columnspan=1, sticky=tkinter.W)

        self._master.bind('<Return>', self.set_address_event)

        self.address.focus()

    def _set_master_properties(self):
        """
        Prepare the master of this object
        """
        ww, wh = 267, 375
        sw, sh = self._master.winfo_screenwidth(), self._master.winfo_screenheight()
        self._master.geometry('%dx%d+%d+%d' % (ww, wh, (sw / 2 - ww / 2), (sh / 2 - wh / 2)))

    def set_address_event(self, event):
        self.set_address()

    def set_address(self):
        address_input = self.address.get()
        if address_input is None or address_input.isspace() or address_input == '':
            return
        self.stored_address = address_input
        self._master.destroy()


if __name__ == '__main__':
    main()
