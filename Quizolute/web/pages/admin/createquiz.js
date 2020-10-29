let questionCount = 1
let answerCount = 1;

const addQuestion = () => {
    const question = (i) => {
        return `
            <div class="card mt-2" id="question-${i}" >
                <div class="card-header">Question #${i}</div>
                <div class="card-body">
                    <h5 class="card-title"> 
                        <textarea placeholder="Write your question here" class="q1" name="question${i}-name" id="question${i}-name" required></textarea> 
                    </h5>
                    <hr/>
                    <div class="card-text">
                        
                    </div>
                    <input hidden id="question${i}-count" name="question${i}-count" value="1"/>
                    <div class="custom-control custom-checkbox col-12" id="question1-choice2" style="background-color: #ececec; border-radius: 5px;">
                        <div class="pb-3 pt-2 text-center">
                            <a class="blue-button" style="font-size:.8em;" onClick="addChoice(${i})">Add New Choice</a>
                        </div>
                    </div>
                    <hr/>
                    <label class="little">Overall Score</label>
                    <input type="number" class="text-box pull-right" id="question${i}-maxScore" name="question${i}-maxScore" value="1"/>
                </div>
            </div>            
        `;
    }
    answerCount = 0
    $(`#question-content`).append(question(questionCount++))
    addChoice(questionCount - 1)
    document.getElementById("questions-count").value++
    
}

const addChoice = (index) => {
        answerCount = $(`#question-${index} > div > div > div`).length
    $(`#question-${index} > div`).find(".card-text").append(`
        <div class="custom-control custom-checkbox col-12" id="question${index}-choice${answerCount}">
            <div class="row pb-3">
                <div class="col-md-2 col-12">
                    <label class="tgl" for="question${index}-isCorrect${answerCount}">
                        <input type="checkbox" id="question${index}-isCorrect${answerCount}" name="question${index}-isCorrect${answerCount}" >
                        <span data-on="Correct" data-off="Wrong">   </span>
                    </label>
                </div>
                <div class="col-md-10 col-12">
                    <input class="form-control" name="question${index}-choice${answerCount}" type="text" placeholder="Choice ${answerCount}" required/>
                </div>
            </div>
        </div>
    `)
    $(`#question${index}-count`)[0].value = answerCount;
    answerCount++
}