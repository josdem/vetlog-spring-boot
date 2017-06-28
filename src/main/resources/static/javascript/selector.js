$("#typeSelector").change(function () {
  var str = $("#typeSelector option:selected").text()
  console.log(str)
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
  }).fail(function(data, status){
    console.log(data, status)
  });
}).change()

