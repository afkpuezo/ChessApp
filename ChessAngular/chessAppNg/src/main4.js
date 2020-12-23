var myExtObject4 = (function() {
    var t = 0;
    var moves = "";
    var loc = "wikipedia"
    var piece = '';
    var w = ''
    var b = ''
    var h = 2
    // var pieces = './assets/img/chesspieces/wikipedia/{piece}.png'
    return {
      create: function(s) {
        // ---------------------------------------------------------------------------------------------------
        
      // document.getElementById('board').style.visibility = 'visible';
      pieces =s;
      var whiteSquareGrey = '#a9a9a9'
      var blackSquareGrey = '#696969'
      var board = null;
      var game = new Chess()
      var $status = $('#status')
      var $mvDiv = $('#mvDiv')  
      
      function removeGreySquares () {
        $('#board .white-1e1d7').css('background', w)
        $('#board .black-3c85d').css('background', b)
      }
      function greySquare (square) {
        if(h%2){
          var $square = $('#board .square-' + square)
      
          var background = whiteSquareGrey
          if ($square.hasClass('black-3c85d')) {
            background = blackSquareGrey
          }
        
          $square.css('background', background)
        }
        
      }
      function onMouseoverSquare (square, piece) {
        // get list of possible moves for this square
        var moves = game.moves({
          square: square,
          verbose: true
        })
      
        // exit if there are no moves available for this square
        if (moves.length === 0) return
      
        // highlight the square they moused over
        greySquare(square)
      
        // highlight the possible squares for this piece
        for (var i = 0; i < moves.length; i++) {
          greySquare(moves[i].to)
        }
      }
      
      function onMouseoutSquare (square, piece) {
        removeGreySquares()
      }
  
      function onDragStart (source, piece, position, orientation) {
        //game end
        if (game.game_over()) return false
      
        // only pick up pieces for the side to move
        if ((game.turn() === 'w' && piece.search(/^b/) !== -1) ||
            (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
          return false
        }
      }
      
      function onDrop (source, target) {
        removeGreySquares()
       
        // see if the move is legal
        var move = game.move({
          from: source,
          to: target,
          promotion: 'q' //promote to queen
        })
      
        // illegal move
        if(move != null){
          var b = ''
          b= source +' '+ target
          //var b = Source + " " + target;
          $mvDiv.html(b)
  
          moves += source
          moves += target
          moves+= " "
          //console.log(moves)
        }
        if (move === null) return 'snapback'
      
        updateStatus()
      }
      
      // update the board position after the piece snap
      function onSnapEnd () {
        board.position(game.fen())
      }
      
      function updateStatus () {
        var status = ''
      
        var moveColor = 'White'
        if (game.turn() === 'b') {
          moveColor = 'Black'
        }
      
        // checkmate
        if (game.in_checkmate()) {
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
        onDragStart: onDragStart,
        onDrop: onDrop,
        onMouseoutSquare: onMouseoutSquare,
        onMouseoverSquare: onMouseoverSquare,
        onSnapEnd: onSnapEnd
    })
  
    updateStatus()
  
  
  
  
  
  
        //-------------------------------------------------CREATE------------------------------------------------------------------------
      },
      func2: function() {
        return t;
      },
      delete: function(){
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
      add: function(){
        c = document.createElement('div');
        c.setAttribute("id", "board2");
        document.body.appendChild(c);
        c = document.createElement('Label');
        c.setAttribute("id", "st2");
        c.innerHTML= "STATUS:"
        document.body.appendChild(c);
        c = document.createElement('div');
        c.setAttribute("id", "status2");
        document.body.appendChild(c);
        
  
        // c = document.createElement('Label');
        // c.setAttribute("id", "mv");
        // c.innerHTML= "LAST MOVE:"
        // document.body.appendChild(c);
        // c = document.createElement('div');
        // c.setAttribute("id", "mvDiv");
        // document.body.appendChild(c);
      },
      getMoves: function(){
        return moves;
      },
      color: function( y){
       
        if(y==1){
          w ='#ffffff';
          b = '#524b4b';
        }
        if(y==2){
          w ='#f64c72';
          b = '#2f2fa2';
        }
        if(y==3){
          w ='#f0d9b5';
          b = '#b58863';
        }
        if(y==4){
          w ='#fccd04';
          b = '#a64ac9';
        }
        if(y==5){
          w ='#a8d0e6';
          b = '#f76c6c';
        }
  
        var x = document.getElementsByClassName("white-1e1d7");
          
          for(var i =0; i<32;i++){
            x[i].style["background-color"] =  w;
          }
  
          var x = document.getElementsByClassName("black-3c85d");
        
          for(var i =0; i<32;i++){
            x[i].style["background-color"] =  b;
          }
  
        
  
        
        
        
      },
      toggle: function(){
        h = h+1
      }
      
    }
  
  })(myExtObject4||{})