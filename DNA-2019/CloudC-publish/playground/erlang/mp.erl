-module(mp).
-export([start_ping/1, start_pong/0, ping/2, pong/0]).

ping(0, PongNode) ->
    {pong, PongNode} ! finished,
    io:format("ping finished~n", []);
ping(N, PongNode) ->
    {pong, PongNode} ! {ping, self()},
    receive
	pong ->
	    io:format("Ping received pong~n", [])
    end,
    ping(N - 1, PongNode).

pong() ->
     receive 
	 finished ->
	     io:format("Pong finished~n", []);
	 {ping, Ping_Node} ->
	     io:format("Pong received ping~n", []),
	     Ping_Node ! pong,
	     pong()
     end. 

start_pong() ->
    register(pong, spawn(mp, pong, [])).

start_ping(PongNode) ->
    spawn(mp, ping, [10, PongNode]).
