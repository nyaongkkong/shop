$(function () {

  const slug = getSlug();
  if (!slug) return;

  $.ajax({
    url: '/api/products/' + encodeURIComponent(slug),
    method: 'GET',
    success: function (res) {
      if (!res || res.success !== true) {
        showError('상품 정보를 불러오지 못했습니다.');
        return;
      }

      renderProduct(res.data);
    },
    error: function (xhr) {
      console.log('product api error:', xhr.status, xhr.responseText);
      showError(xhr?.responseJSON?.error?.message || '서버 오류');
    }
  });

});

function renderProduct(p) {

  $('#productName').text(p.name);
  $('#productBrand').text(p.brand.name);
  $('#productBrandName').text(p.brand.name);
  $('#productCategory').text(p.category.name);

  $('#productPrice').text(formatPrice(p.price));

  if (p.thumbnailUrl) {
    $('#productImage').html(
      `<img src="${escapeHtml(p.thumbnailUrl)}"
            style="width:100%; height:100%; object-fit:cover;">`
    );
  }

  $('#buyBtn').on('click', function () {
    alert('구매 기능은 다음 단계에서!');
  });
}

function getSlug() {
  const path = window.location.pathname;
  const parts = path.split('/').filter(Boolean);
  // /products/{slug}
  return parts.length === 2 ? parts[1] : null;
}

function formatPrice(price) {
  const n = typeof price === 'number' ? price : Number(price);
  if (Number.isFinite(n)) return n.toLocaleString('ko-KR');
  return price;
}

function showError(msg) {
  $('#productName').text(msg);
}

function escapeHtml(str) {
  if (!str) return '';
  return String(str)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}