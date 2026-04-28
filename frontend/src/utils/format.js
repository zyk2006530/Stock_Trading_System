export function formatMoney(value) {
  if (value === null || value === undefined) {
    return '--';
  }
  const num = Number(value);
  if (Number.isNaN(num)) {
    return '--';
  }
  return num.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}
