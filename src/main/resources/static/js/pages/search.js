$(function () {
  const q = getQueryParam('q') || '';
  if (!q.trim()) {
    $('#searchTitle').text('검색');
    renderEmpty('검색어를 입력해 주세요.');
    return;
  }

  $('#searchTitle').text("‘" + q + "’ 검색 결과");

  $.ajax({
    url: '/api/search',
    method: 'GET',
    data: { q },
    success: function (res) {
      if (!res || res.success !== true) {
        renderEmpty('검색 결과를 불러오지 못했습니다.');
        return;
      }
      renderBrands(res.data.brands || []);
      renderProducts(res.data.products || []);
    },
    error: function (xhr) {
      console.log('search api error:', xhr.status, xhr.responseText);
      renderEmpty(xhr?.responseJSON?.error?.message || '서버 오류가 발생했습니다.');
    }
  });
});

function renderBrands(brands) {
  const $grid = $('#brandGrid');
  $grid.empty();

  if (!brands.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">일치하는 브랜드가 없습니다.</div>`);
    return;
  }

  brands.forEach(b => {
    const thumb = b.logoUrl
      ? `<img src="${escapeHtml(b.logoUrl)}" alt="" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>${escapeHtml(b.name || '')}</span>`;

    const cardHtml = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-name">${escapeHtml(b.name || '')}</div>
        <div class="product-brand">바로가기</div>
      </div>
    `;

    const $card = $(cardHtml);
    $card.on('click', function () {
      // 브랜드 페이지가 없으면 일단 검색 유지/또는 카테고리로 연결 가능
      location.href = '/brands/' + encodeURIComponent(b.slug);
    });

    $grid.append($card);
  });
}

function renderProducts(products) {
  const $grid = $('#productGrid');
  $grid.empty();

  if (!products.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">일치하는 상품이 없습니다.</div>`);
    return;
  }

  products.forEach(p => {
    const thumb = p.thumbnailUrl
      ? `<img src="${escapeHtml(p.thumbnailUrl)}" alt="" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>NO IMAGE</span>`;

    const priceText = formatPrice(p.price);

    const cardHtml = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-brand">${escapeHtml(p.brandName || '')}</div>
        <div class="product-name">${escapeHtml(p.name || '')}</div>
        <div class="product-price">
          <span>${priceText}</span>
          <span class="product-price-label">원</span>
        </div>
      </div>
    `;

    const $card = $(cardHtml);
    $card.on('click', function () {
      location.href = '/products/' + encodeURIComponent(p.slug);
    });

    $grid.append($card);
  });
}

function renderEmpty(msg) {
  $('#brandGrid').html(`<div style="color:#888; font-size:13px;">${escapeHtml(msg)}</div>`);
  $('#productGrid').empty();
}

function getQueryParam(key) {
  const params = new URLSearchParams(window.location.search);
  return params.get(key);
}

function formatPrice(price) {
  const n = typeof price === 'number' ? price : Number(price);
  if (Number.isFinite(n)) return n.toLocaleString('ko-KR');
  return (price == null ? '' : String(price));
}

function escapeHtml(str) {
  if (str == null) return '';
  return String(str)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}
