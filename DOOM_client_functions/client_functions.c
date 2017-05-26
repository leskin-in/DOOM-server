#include <pthread.h>
#include "client_functions.h"
#include "dconnect.h"
#include "dconnect_settings.h"


void ClientStartGame() {

  pack_to_send = make_UPACK(sizeof(keyboard_key));
  tick = 1;
  pack_to_send->type = DP_CLIENT_ACTION;



  initscr();
  start_color();
  nodelay(stdscr, true);
  keypad(stdscr, true);
  noecho();

  pthread_t send_thread;
  pthread_create(&send_thread, NULL, SendData, NULL);

  pthread_t receive_render_thread;
  pthread_create(&receive_render_thread, NULL, GetData_and_RenderScreen, NULL);


  pthread_join(send_thread, NULL);
  pthread_join(receive_render_thread, NULL);

  endwin();
  return;
}

void* SendData() {
  while (true) {
    keyboard_key command;
    tick++;
    pack_to_send->stamp = tick;

    //get keyboard command and send it to the server
    command = getch();
    switch (command) {
    case forward:
      pack_to_send->data[0] = forward;
      break;

    case left:
      pack_to_send->data[0] = left;
      break;

    case backward:
      pack_to_send->data[0] = backward;
      break;

    case right:
      pack_to_send->data[0] = right;
      break;

    case bomb:
      pack_to_send->data[0] = bomb;
      break;

    case quit:
      pack_to_send->data[0] = quit;
      break;

    default:
      pack_to_send->data[0] = -1;
    }

    d_client_send(pack_to_send, UPACK_SIZE(sizeof(int)), NET_REPEAT_CLIENT);

    if (command == quit) {
      endwin();
      break;
    }
    refresh();
  }
  return NULL;
}


void* GetData_and_RenderScreen() {

  init_pair(1, COLOR_WHITE, COLOR_BLACK);
  attron(COLOR_PAIR(1));
  while (true) {
    //get data
    d_client_get(pack_to_receive, screenRows * screenColumns * sizeof(ConsoleSymbolData),
                tick);
    if (pack_to_receive->type == DP_CLIENT_STOP) {
      printw("The game is over");
      return NULL;
    }
    //render it
    int row = 0;
    int column = 0;
    init_pair(1, COLOR_WHITE, COLOR_BLACK);
    attron(COLOR_PAIR(1));
    for (row; row < screenRows; ++row) {
      for (column; column < screenColumns; ++column) {
        printw("%c", pack_to_receive->data[row][column].symbol);
      }
    }

    refresh();

    //if signal to finish - break;
  }
  attroff(COLOR_PAIR(1));
  return NULL;
}

