class MathProblem {
    constructor(botValue, topValue, opSign) {
      this.botValue = botValue;
      this.topValue = topValue;
      this.opSign = opSign;
    }

    // returns the answer of this math problem
    getAnswer() {
      switch (this.opSign) {
        case "+":
          return this.topValue + this.botValue;
        case "-":
          return this.topValue - this.botValue;
        case "*":
          return this.topValue * this.botValue;
        case "/":
          return this.topValue / this.botValue;
        default:
          return "Undefined Sign";
      }
    }
}

var startTimer;
let opList = [];
var mathProb;



function start() {
    // check if the check boxes are checked or not
    if (opList.length < 1) {
        mesBoardUpdate("Choose at least one math operation.");
        return 0;
    }

    // check if there is a number range
    let lowRange = parseInt(document.querySelector("#low-input").value);
    let highRange = parseInt(document.querySelector("#high-input").value);

    if (!Number.isInteger(lowRange) || !Number.isInteger(highRange)) {
        mesBoardUpdate("The low and high range numbers must be a number only.");
        return 0;
    }

    if (lowRange < 0) {
        mesBoardUpdate("Lowest number for the low range is 0.");
        return 0;
    }

    if (highRange < lowRange) {
        mesBoardUpdate("Number for the high range must be larger than the low range number.");
        return 0;
    }

    // check if timer is being used
    var timerCheckbox = document.querySelector("#timer-checkbox");
    var timerCheck = 1;
    if (timerCheckbox.checked) {
        timerCheck = timerCountDown();
    }

    // generate math problem
    if (timerCheck == 1) {
        generateMathProb();
    }


}

function submit() {
    // checks if there is an answer or not
    var answer = parseFloat(document.querySelector("#input-box").value);
    
    if (!Number.isInteger(answer)) {
        mesBoardUpdate("Answer must be a number.");
    }

    // compares answer to math problem answer
    // correct = update score, message and generates new problem
    // wrong = update score and message

    if (answer == mathProb.getAnswer()) {
        updateScore(true);
        mesBoardUpdate("Correct!");
        generateMathProb();
    }
    else {
        updateScore(false);
        mesBoardUpdate("Wrong! Answer is " + mathProb.getAnswer());
    }
}

// start the timer countdown
// returns 0 if timer checkbox is checked, returns 1 if it isn't
function timerCountDown() {
    let timerValue = document.querySelector("#sec-input");

     // check if timerValue is a number or not
     if (!Number.isInteger(parseInt(timerValue.value))) {
        mesBoardUpdate("Timer is not set to a number.");
        return 0;
    }

    startTimer = setInterval(function() {  
        if (timerValue.value > 0) {
            timerValue.value = parseInt(timerValue.value) - 1;
        }
        else {
            clearInterval(startTimer);
            alert("Time's up!");
        }
        }, 1000);
    return 1;
}

// creates a random math problem
function generateMathProb() {
    let lowRange = parseInt(document.querySelector("#low-input").value);
    let highRange = parseInt(document.querySelector("#high-input").value);

    var lowNum = generateRandNum(parseInt(lowRange), parseInt(highRange));
    var highNum = generateRandNum(parseInt(lowNum), parseInt(highRange));
    var sign = opList[Math.floor(Math.random() * opList.length)];
    var opSign;
    
    switch (sign) {
        case "add-checkbox":
          opSign = "+";
          break;
        case "sub-checkbox":
            opSign = "-";
            break;
        case "mul-checkbox":
            opSign = "*";
            break;
        case "div-checkbox":
            opSign = "/";
            break;
        default:
          opSign = "Undefined Sign";
    }
    mathProb = new MathProblem(lowNum, highNum, opSign);

    let topValue = document.querySelector("#top-value");
    let botValue = document.querySelector("#bot-value");
    let signValue = document.querySelector('#op-value');

    topValue.innerHTML = mathProb.topValue;
    botValue.innerHTML = mathProb.botValue;
    signValue.innerHTML = mathProb.opSign;
}

// generates a random between the two numbers
function generateRandNum(lowRange, highRange) {
    return Math.floor(Math.random() * (highRange - lowRange + 1) + lowRange);
}

// stops the timer countdown
function stopTimer() {
    clearInterval(startTimer);
}

// updates the score depending if the answer is correct
function updateScore(isAnswerCorrect) {
    if (isAnswerCorrect == true) {
        var correctScore = document.querySelector("#correct-score");
        correctScore.innerHTML = (parseInt(correctScore.innerHTML) + 1);
    }
    else {
        var wrongScore = document.querySelector("#wrong-score");
        wrongScore.innerHTML = (parseInt(wrongScore.innerHTML) + 1);
    }
}

// updates the op list if the check box is clicked or not
function updateOpList(id) {
    if (document.querySelector("#" + id).checked) {
        opList.push(id);
    }
    else {
        // removes an object from an array
        var objIdx = opList.indexOf(id);
        if (objIdx > -1) {
            opList.splice(objIdx, 1);
        }
    }
}

// resets the scores when the reset button is pressed
function resetScore() {
    document.querySelector("#correct-score").innerHTML = 0;
    document.querySelector("#wrong-score").innerHTML = 0;
}

// changes the message on the message board
function mesBoardUpdate(mes) {
    document.querySelector("#mes-board").innerHTML = mes;
}

// adds event listeners to buttons
const submitBtn = document.querySelector("#submit-btn");
const resetBtn = document.querySelector("#reset-btn");
const startBtn = document.querySelector("#start-btn");
const stopBtn = document.querySelector("#stop-btn");

submitBtn.addEventListener('click', submit);
resetBtn.addEventListener('click', resetScore);
startBtn.addEventListener('click', start);
stopBtn.addEventListener('click', stopTimer);

