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
      data.forEach(function(it){
        console.log("id: " + it.id + " value: " + it.name)
      })
      $("#breedSelector").empty()
    }).fail(function(data, status){
      console.log(data, status)
    });
}).change()
