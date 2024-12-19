// ... 其他导入

// 获取通知列表
router.get('/notifications', (req, res) => {
  const { userId } = req.query
  const notifications = db.get('notifications')
    .filter(n => n.userId === userId)
    .value()
  res.json(notifications)
})

// 获取未读消息数量
router.get('/notifications/unread/count', (req, res) => {
  const count = db.get('notifications')
    .filter(n => !n.isRead)
    .size()
    .value()
  res.json(count)
})

// 标记消息为已读
router.put('/notifications/:id', (req, res) => {
  const { id } = req.params
  db.get('notifications')
    .find({ id: parseInt(id) })
    .assign({ isRead: true })
    .write()
  res.json({ success: true })
})

// 标记所有消息为已读
router.put('/notifications/read-all', (req, res) => {
  db.get('notifications')
    .forEach(n => {
      n.isRead = true
    })
    .write()
  res.json({ success: true })
})

// ... 其他路由 