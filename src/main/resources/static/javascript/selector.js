$("#typeSelector").on('change', function () {
  var type = $("#typeSelector option:selected").text()
  var url = $("#breedsByTypeUrl").val()
  window.location = url + "?type=" + type
})
