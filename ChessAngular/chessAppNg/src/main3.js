
var myExtObject2 = (function () {
  var t = 0;
  var moves = "";
  var loc = "wikipedia"
  var piece = '';
  var w = ''
  var b = ''
  var h = 2
  var code = '';
  var board = null;
  var game = new Chess()
  var un = "";
  var tx = 0;
  // var pieces = './assets/img/chesspieces/wikipedia/{piece}.png'
  return {
    create: async function (s, ori) {
      //test

      //await new Promise(r => setTimeout(r, 2000));
      // ---------------------------------------------------------------------------------------------------
      game.reset()
      // document.getElementById('board').style.visibility = 'visible';
      pieces = s;
      var whiteSquareGrey = '#a9a9a9'
      var blackSquareGrey = '#696969'

      var $status = $('#status')
      var $mvDiv = $('#mvDiv')

      function removeGreySquares() {
        $('#board .white-1e1d7').css('background', w)
        $('#board .black-3c85d').css('background', b)
      }
      function greySquare(square) {
        if (h % 2) {
          var $square = $('#board .square-' + square)

          var background = whiteSquareGrey
          if ($square.hasClass('black-3c85d')) {
            background = blackSquareGrey
          }

          $square.css('background', background)
        }

      }
      function onMouseoverSquare(square, piece) {
        // get list of possible moves for this square
        var moves2 = game.moves({
          square: square,
          verbose: true
        })

        // exit if there are no moves available for this square
        if (moves2.length === 0) return

        // highlight the square they moused over
        greySquare(square)

        // highlight the possible squares for this piece
        for (var i = 0; i < moves2.length; i++) {
          greySquare(moves2[i].to)
        }
      }

      function onMouseoutSquare(square, piece) {
        removeGreySquares()
      }

      function onDragStart(source, piece, position, orientation) {
        //game end
        if (game.game_over()) return false

        // only pick up pieces for the side to move
        if (ori == 'white') {
          if (piece.search(/^b/) !== -1) {
            return false
          }
        } else {
          if (piece.search(/^w/) !== -1) {
            return false
          }
        }

      }

      function makeRandomMove() {
        var possibleMoves = game.moves()

        // game over
        if (possibleMoves.length === 0) return

        var randomIdx = Math.floor(Math.random() * possibleMoves.length)
        //console.log(possibleMoves[randomIdx])
        game.move(possibleMoves[randomIdx])
        board.position(game.fen())
      }

      async function oppGo() {
        updateStatus()
        let xhr = new XMLHttpRequest()
        let template = {
          user: un,
          code: code
        }
        xhr.onreadystatechange = function () {
          if (this.readyState === 4 && this.status === 200) {
            //console.log(moves)
            moves = this.responseText
            var split = moves.split(" ");
            var l = split.length;
            //var f = split.substring(l-4,l-2)
            //var a = split.substring(l-2,l)
            //console.log(split[l-1])
            var f = split[l - 1].substring(0, 2)
            var t = split[l - 1].substring(2, 4)
            var move = game.move({
              from: f,
              to: t,
              promotion: 'q' //promote to queen
            })

            if (move == null) {
              window.setTimeout(d2, 1000)
            }
            //await new Promise(r => setTimeout(r, 2000));
            updateStatus()
            board.position(game.fen())

          }

        }
        xhr.open("POST", "http://localhost:8080/ChessApp/getMove")
        xhr.send(JSON.stringify(template))

        function d2() {
          console.log("hi d2 oppg")
          updateStatus()
          xhr.open("POST", "http://localhost:8080/ChessApp/getMove")
          xhr.send(JSON.stringify(template))
        }
        //await myExtObject2.sleep(2000)




      }

      function onDrop(source, target) {
        //myExtObject2.te()

        removeGreySquares()



        // see if the move is legal
        var move = game.move({
          from: source,
          to: target,
          promotion: 'q' //promote to queen
        })

        // illegal move
        if (move != null) {
          var b = ''
          b = source + ' ' + target
          //var b = Source + " " + target;
          $mvDiv.html(b)
          moves += " "
          moves += source
          moves += target


          let xhr = new XMLHttpRequest()
          let template = {
            code: code,
            moves: moves
          }

          xhr.open("POST", "http://localhost:8080/ChessApp/recordMove")
          xhr.send(JSON.stringify(template))
          //console.log(moves)
        }
        if (move === null) return 'snapback'

        updateStatus()
        window.setTimeout(oppGo, 300)
      }

      // update the board position after the piece snap
      function onSnapEnd() {
        board.position(game.fen())
      }

      function updateStatus() {
        var status = ''

        var moveColor = 'White'
        if (game.turn() === 'b') {
          moveColor = 'Black'
        }

        // checkmate
        if (game.in_checkmate()) {
          console.log('checkmate')
          console.log(un)
          console.log
          console.log(tx)
          console.log(moveColor)
          if (tx == 2 && moveColor == 'White') {
            console.log("black winner")
            console.log(un)
            console.log(code)
            let xhr = new XMLHttpRequest()

            let template = {
              code: code,
              user: un
            }
            xhr.onreadystatechange = function () {

            }
            xhr.open("POST", "http://localhost:8080/ChessApp/recordGameWinner")
            xhr.send(JSON.stringify(template))
          }

          if (tx == 1 && moveColor == 'Black') {
            let xhr = new XMLHttpRequest()
            let template = {
              code: code,
              user: un
            }
            xhr.onreadystatechange = function () {

            }
            xhr.open("POST", "http://localhost:8080/ChessApp/recordGameWinner")
            xhr.send(JSON.stringify(template))
          }
          //console.log(s)
          status = 'Game over, ' + moveColor + ' is in checkmate.'
        }

        // draw?
        else if (game.in_draw()) {
          //console.log(s)
          status = 'Game over, drawn position'
        }


        else {
          status = moveColor + ' turn'


          if (game.in_check()) {
            status += ', ' + moveColor + ' is in check'
          }
        }

        $status.html(status)

      }
      board = ChessBoard('board', {
        draggable: true,
        position: 'start',
        pieceTheme: pieces,
        orientation: ori,
        onDragStart: onDragStart,
        onDrop: onDrop,
        onMouseoutSquare: onMouseoutSquare,
        onMouseoverSquare: onMouseoverSquare,
        onSnapEnd: onSnapEnd
      })

      updateStatus()






      //-------------------------------------------------CREATE------------------------------------------------------------------------
    },
    setUsername: function (s) {
      un = s;

      console.log(un)
    },
    sleep: async function (x) {
      await new Promise(r => setTimeout(r, x));
    },
    func2: function () {
      return t;
    },
    delete: function () {
      var b = document.getElementById("board");
      b.remove()
      var b1 = document.getElementById("st");
      b1.remove()
      var b2 = document.getElementById("status");
      b2.remove()
      var b3 = document.getElementById("mv");
      b3.remove()
      var b4 = document.getElementById("mvDiv");
      b4.remove()
    },
    add: function () {
      c = document.createElement('div');
      c.setAttribute("id", "board");
      document.body.appendChild(c);
      c = document.createElement('Label');
      c.setAttribute("id", "st");
      c.innerHTML = "STATUS:"
      document.body.appendChild(c);
      c = document.createElement('div');
      c.setAttribute("id", "status");
      document.body.appendChild(c);

      c = document.createElement('Label');
      c.setAttribute("id", "mv");
      c.innerHTML = "LAST MOVE:"
      document.body.appendChild(c);
      c = document.createElement('div');
      c.setAttribute("id", "mvDiv");
      document.body.appendChild(c);
    },
    getMoves: function () {
      return moves;
    },
    color: function (y) {

      if (y == 1) {
        w = '#ffffff';
        b = '#524b4b';
      }
      if (y == 2) {
        w = '#f64c72';
        b = '#2f2fa2';
      }
      if (y == 3) {
        w = '#f0d9b5';
        b = '#b58863';
      }
      if (y == 4) {
        w = '#fccd04';
        b = '#a64ac9';
      }
      if (y == 5) {
        w = '#a8d0e6';
        b = '#f76c6c';
      }

      var x = document.getElementsByClassName("white-1e1d7");

      for (var i = 0; i < 32; i++) {
        x[i].style["background-color"] = w;
      }

      var x = document.getElementsByClassName("black-3c85d");

      for (var i = 0; i < 32; i++) {
        x[i].style["background-color"] = b;
      }






    },
    toggle: function () {
      h = h + 1
    },
    makeGame: function (c) {
      tx = 1;
      code = c

      let xhr = new XMLHttpRequest()
      let template = {
        whiteUser: un,
        code: code
      }
      xhr.onreadystatechange = function () {

      }
      xhr.open("POST", "http://localhost:8080/ChessApp/makeGame")
      xhr.send(JSON.stringify(template))
      //this.te()
    },
    setNull: function () {
      moves = "";
    },
    test: function () {
      console.log("test")
    },
    te: function () {
      console.log("te")
    },
    setCode: function (c) {
      code = c
      console.log(code)
    },
    firstMove: function () {
      let xhr = new XMLHttpRequest()
      let template = {
        user: un,
        code: code
      }
      xhr.onreadystatechange = function () {
        moves = "";
        if (this.readyState === 4 && this.status === 200) {
          //console.log(moves)
          moves = this.responseText
          if (moves == "" || moves == null) {
            window.setTimeout(d, 1000)
          }
          var split = moves.split(" ");
          var l = split.length;
          //var f = split.substring(l-4,l-2)
          //var a = split.substring(l-2,l)
          //console.log(split[l-1])
          var f = split[l - 1].substring(0, 2)
          var t = split[l - 1].substring(2, 4)
          //console.log(f)
          //console.log(t)
          var move = game.move({
            from: f,
            to: t,
            promotion: 'q' //promote to queen
          })

          board.position(game.fen())
        }

      }
      xhr.open("POST", "http://localhost:8080/ChessApp/getMove")
      xhr.send(JSON.stringify(template))
      console.log("first move")
      function d() {
        console.log("hi d")
        xhr.open("POST", "http://localhost:8080/ChessApp/getMove")
        xhr.send(JSON.stringify(template))

      }

      window.setTimeout(d, 1000)



    },
    findGame: function () {
      tx = 2;
      console.log(code)
      let xhr = new XMLHttpRequest()
      let template = {
        user: un,
        code: code
      }

      xhr.open("POST", "http://localhost:8080/ChessApp/findGame")
      xhr.send(JSON.stringify(template))
    }

  }

})(myExtObject2 || {})