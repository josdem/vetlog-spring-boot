$("#typeSelector").change(function () {
  var str = $("#typeSelector option:selected").text()
    console.log(str)
    $.ajax($("#findClientUrl").val(), {
      method: 'GET',
      dataType: 'json',
      data: {
        rfc: $('#rfc').val()
      }
    }).done(function(data) {
      console.log(data)
    }).fail(function(data, status){
      console.log(data, status)
    });
}).change()
