const updateBreedType = () => {
  let str = $("#typeSelector option:selected").text()
  console.log("typeSelector: " + str)
  $.ajax($("#breedsByTypeUrl").val(), {
    method: 'GET',
    dataType: 'json',
    data: {
        type: str
    }
    }).done(function(data) {
      $("#breedSelector").empty()
      data.forEach(function(it){
        $("#breedSelector").append($("<option></option>").attr("value", it.id).text(it.name))
      })
      $("#breedSelector").val($("#breed").val())
    }).fail(function(data, status){
      console.log(data, status)
    })
}

