var myExtObject = (function() {
  var t = 1;
  var turn = 0;
  var moves = "";
  var loc = "wikipedia"
  var piece = '';
  var w = ''
  var b = ''
  var h = 2
  var l
  var split;
  var board2 = null;
  var game2 = new Chess()
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
    create2: function(s){
       // ---------------------------------------------------------------------------------------------------
      
    // document.getElementById('board').style.visibility = 'visible';
    pieces =s;
    
    
    var $status = $('#status')
    var $mvDiv = $('#mvDiv')  
    
    
    
    function updateStatus () {
      var status = ''
    
      var moveColor = 'White'
      if (game2.turn() === 'b') {
        moveColor = 'Black'
      }
    
      // checkmate
      if (game2.in_checkmate()) {
        //console.log(s)
        status = 'Game over, ' + moveColor + ' is in checkmate.'
      }
    
      // draw?
      else if (game2.in_draw()) {
        //console.log(s)
        status = 'Game over, drawn position'
      }
    
      
      else {
        status = moveColor + ' turn'
    
        
        if (game2.in_check()) {
          status += ', ' + moveColor + ' is in check'
        }
      }
    
      $status.html(status)
      
    }
    board2 = ChessBoard('board', {
    
      position: 'start',
      pieceTheme: pieces,
  })

  updateStatus()


    },
    find: function(code,un){
      let xhr = new XMLHttpRequest()
      let template = {
        user: un,
        code: code
       }
      xhr.onreadystatechange = function(){
        if(this.readyState ===4 && this.status ===200){
          //console.log(moves)
        moves = this.responseText
        split = moves.split(" ");
        l= split.length
        console.log(moves)
        // var split = moves.split(" ");
        // var l = split.length;
        // //var f = split.substring(l-4,l-2)
        // //var a = split.substring(l-2,l)
        // //console.log(split[l-1])
        // var f = split[l-1].substring(0,2)
        // var t = split[l-1].substring(2,4)
        // var move = game.move({
        //   from: f,
        //   to: t,
        //   promotion: 'q' //promote to queen
        // })

        // if(move == null){
        //   window.setTimeout(d2,3000)
        // }
        // //await new Promise(r => setTimeout(r, 2000));

        // board.position(game.fen())
        
        }
        
      }
      xhr.open("POST","http://localhost:8080/ChessApp/getMove")
      xhr.send(JSON.stringify(template))
    },
    next: function(){
      console.log(t)
     
      console.log(moves)
      
       var f = split[t].substring(0,2)
       console.log(f)
       var t2 = split[t].substring(2,4)
       console.log(t2)
      
      var move = game2.move({
        from: f,
        to: t2,
        promotion: 'q' //promote to queen
      })


      board2.position(game2.fen())
      t= t+1;
      if(t>l-1){
        t = l-1
      }

    },
    prev: function(){
      console.log(t)
      game2.undo()
      board2.position(game2.fen())
      t = t-1;
      if(t<1){
        t = 1;
      }
    },
    func2: function() {
      return t;
    },
    delete: function(x){
      var b = document.getElementById("board");
      b.remove()
      if(x==1){
        var b1 = document.getElementById("st");
      b1.remove()
      var b2 = document.getElementById("status");
      b2.remove()
      var b3 = document.getElementById("mv");
      b3.remove()
      var b4 = document.getElementById("mvDiv");
      b4.remove()
      }
    },
    add: function(x){
      c = document.createElement('div');
      c.setAttribute("id", "board");
      document.body.appendChild(c);
      if(x==1){
        c = document.createElement('Label');
      c.setAttribute("id", "st");
      c.innerHTML= "STATUS:"
      document.body.appendChild(c);
      c = document.createElement('div');
      c.setAttribute("id", "status");
      document.body.appendChild(c);

      c = document.createElement('Label');
      c.setAttribute("id", "mv");
      c.innerHTML= "LAST MOVE:"
      document.body.appendChild(c);
      c = document.createElement('div');
      c.setAttribute("id", "mvDiv");
      document.body.appendChild(c);
      }
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

})(myExtObject||{})